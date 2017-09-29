#!/bin/bash

TA_MODE=false

PREPARING=false
CLEANING=false

declare -a SAMPLE_TEST_CASES=('configuration_test_fail' \
'standard_test' \
'according_to_project_description' \
'two_game_history');

function try() { [[ $- = *e* ]]; SAVED_OPT_E=$?;set +e;}
function throw() { exit $1;}
function catch() { export ex_code=$?;(( $SAVED_OPT_E )) && set +e;return $ex_code;}
function enable_throwing_errors() { set -e;}
function disable_throwing_errors() { set +e;}

main_dir=$(pwd)
submissions_dir="$main_dir/submissions"
tests_dir="$main_dir/test_cases"
brief_feedback_file="$main_dir/brief_results.csv"
detailed_feedback_dir="$main_dir/detailed_feedback"

collect_results()
{
	mkdir -p "$detailed_feedback_dir"
	cd "$submissions_dir"
	for tested_submission in */
	do
		tested_submission=${tested_submission:0:${#tested_submission}-1}
		zip -ry9Tm "$tested_submission" "$tested_submission" > /dev/null
		mv "$tested_submission".zip "$detailed_feedback_dir"

	done
	cd "$main_dir"
}

perform_test_on_this_submission()
{
    enable_throwing_errors

	this_dir=$(pwd)
    total_num=0
    correct_num=0
    submission_name="${PWD##*/}"
    for t_dir in "$tests_dir"/*/
    do
        cp "$t_dir/GameConfiguration.java" assignment2/ 
        cp "$t_dir/SecretCodeGenerator.java" assignment2/

        cd assignment2
        javac *.java
        cd ..

        total_num=$(($total_num+1))
        touch temp_output.txt
        java assignment2.Driver < "$t_dir/input.txt" > temp_output.txt 2>&1 || echo "nothing" > /dev/null
        sed '/^\s*$/d' temp_output.txt > actual_output.txt
        sed '/^\s*$/d' "$t_dir/expected_output.txt" > temp_output.txt
        printf "\n\n\n$t_dir\n" >> output_diff.txt

        if [[ $TA_MODE == true ]]; then
            diff -uN temp_output.txt actual_output.txt >> output_diff.txt &&
                correct_num=$(($correct_num+1)) || echo "error on $t_dir" > /dev/null
        else
            diff -uN temp_output.txt actual_output.txt >> output_diff.txt &&
                correct_num=$(($correct_num+1)) || echo "error on $t_dir"
        fi

    done
    rm -rf assignment2 temp_output.txt actual_output.txt
    echo "\"$submission_name\", \"$correct_num\", \"$total_num\"" >> "$brief_feedback_file"
}

run_test_on_submissions()
{
    enable_throwing_errors

	cd "$submissions_dir"

    if ( ls *.zip 1> /dev/null 2>&1 )
    then
	    for submission_raw_name in *.zip
	    do
	    	submission_name="${submission_raw_name%%.*}"
            try
            (
	    	    submission_name="${submission_raw_name%%.*}"
                echo ""
	    	    echo "### going on: " "$submission_name"

	    	    mkdir "$submission_name"
	    	    cd "$submission_name"
	    	    unzip ../"$submission_raw_name" > /dev/null
	    	    find . -name __MACOSX -exec rm -rfv {} \; > /dev/null 2>&1 || echo ""
	    	    rm ../"$submission_raw_name"
                mkdir "temp_java_files"

	            java_files="$(find . -name '*.java')"
                for jfile in $java_files
                do
	                mv "$jfile" "temp_java_files"
                done
                mv "temp_java_files" "/tmp"
                rm -rf *
                mv "/tmp/temp_java_files" "./assignment2"

	    	    perform_test_on_this_submission

	    	    cd "$submissions_dir"
            )
            catch ||
            {
                echo "###### submission format / compilation error on $submission_name"
                echo "\"$submission_name\", \"0\", \"0\"" >> "$brief_feedback_file"
                cd "$submissions_dir"
            }
	    done
    fi

	cd "$main_dir"
}

grade()
{
    enable_throwing_errors

    if [[ ! -f "$brief_feedback_file" ]]
    then
	    rm -rf  "$detailed_feedback_dir"
        echo '"submission_name", "passed_tests", "total_tests"' > "$brief_feedback_file"
    fi

	run_test_on_submissions
	echo "collecting results ..."
	collect_results
	echo "finished"

    disable_throwing_errors
}

main()
{
    if [[ $CLEANING == true ]]; then
        clean
    elif [[ $PREPARING == true ]]; then
        prepare
    else
        grade
    fi
}

prepare()
{
    if [[ $TA_MODE == true ]]; then
        rm -rf test_cases
        git checkout test_cases
    else
        mkdir "sample_tests"
        for tname in "${SAMPLE_TEST_CASES[@]}"; do
            mv "test_cases/$tname" "sample_tests"
        done
        rm -rf "test_cases"
        mv "sample_tests" "test_cases"
    fi
}

clean()
{
    rm -rf detailed_feedback brief_results.csv
    rm -rf submissions test_cases
    git checkout -- submissions test_cases
}

while [[ $# > 0 ]]
do
    arg="$1"
    case $arg in
        --ta)       TA_MODE=true;;
        --clean)    CLEANING=true;;
        --prepare)  PREPARING=true;;
    esac

    shift
done

main
