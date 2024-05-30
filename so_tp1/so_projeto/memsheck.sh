#!/bin/bash

make
valgrind --leak-check=full --show-reachable=yes --track-origins=yes -s ./kp 1 ex05.txt 10 10