#!/bin/bash

git log --format='%aN' | sort -u | while read author; do
    echo "Contributor: $author"
    echo "------------------"
    git log --author="$author" --oneline --shortstat | \
        awk '/files? changed/ {files+=$1; inserted+=$4; deleted+=$6} \
             END {print "Files changed:", files; \
                  print "Lines inserted:", inserted; \
                  print "Lines deleted:", deleted}'
    echo "------------------"
done
