#!/usr/bin/env bash

# ${OPTARG}
show_usage () {
    echo "Multi-usage script for the project"
    echo "Usage: $(basename "$0") [-b/-j/-t/-p/-h]"
    echo "-b    Build and run the CLI*"
    echo "-g    Build and run the GUI*"
    echo "-j    Generate the JavaDoc"
    echo "-t    Run the JUnit tests"
    echo "-p    Package in a .jar file"
    echo "-h    Show this help menu"
    echo -e "\n*: Warning, requires Java 14+\n"
}

if [ $# -ge 1 ]; then
    while getopts bgjtph flag; do
        case "$flag" in
            b) # Build and run CLI
                find . -name "*.java" > ./files.txt
                javac -d bin @files.txt -encoding UTF-8
                rm -f ./files.txt
                java -cp bin MusicHub
            ;;
            g) # Build and run GUI
                find . -name "*.java" > ./files.txt
                javac -d bin @files.txt -encoding UTF-8
                rm -f ./files.txt
                java -cp bin MusicHubGUI
            ;;
            j) # JavaDoc generation
                javadoc -d javadoc lethimonnier.antoine.jmusichub
            ;;
            t) # Running JUnit Tests
                java -jar junit-platform-console-standalone-1.8.0-M1.jar
            ;;
            p) # Packaging in jar
                mvn package
            ;;
            h) # Usage
                show_usage
            ;;
            *) echo "Invalid option: -$flag" ;;
        esac
    done
else
    show_usage
fi