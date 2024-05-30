#!/bin/bash

#create folder if not exists
if ! [ -d "out/" ]; then
    mkdir out
fi

#configs
file="ex25" #file to load
nProcess=10  #amount of process to task
time=20 #amount of time to spend with task

echo "#Test|Test File (#Items)|Execution Time|#Processes|Best Value|Weights Sum.|Achieved Best|#Iterations|Time to Best Value" > out/${file}_test.txt
i=1

while [ $i -le 10 ]
do
	./kp $i ${file}.txt $nProcess $time >> out/${file}_test.txt
	echo "Entry $i created!"
	i=$(( i+1 ))
done

countAchieved=0
avgTime=0
avgIterations=0

while IFS="|" read -r f1 f2 f3 f4 f5 f6 f7 f8 f9
do
  if ! [ "$f1" = "#Test" ]; then
    var="$f7"
    var="${var#"${var%%[![:space:]]*}"}"
    if [ $var = "Yes" ]; then
        countAchieved=$((countAchieved+1))
    fi
    avgIterations=$((avgIterations + $f8))
    var="$f9"
    var="${var#"${var%%[![:space:]]*}"}"
    avgTime=$((avgTime+var))
  fi
done < out/${file}_test.txt
avgIterations=$((avgIterations / 10))
avgTime=$((avgTime / 10))
printf '\t\t\t\t\t\t\t\tAchieved Times: %d|Avg. Iterations: %d|Avg. Time: %d\n' "$countAchieved" "$avgIterations" "$avgTime" >> out/${file}_test.txt

echo "File \"${file}_test.txt\" saved to \"/out\"!"