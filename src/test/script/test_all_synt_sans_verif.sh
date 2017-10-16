#!/bin/sh

for i in ../deca/syntax/valid/*.deca
do
    echo "$i"
    ./launchers/test_synt "$i"
done

for i in ../deca/syntax/invalid/*.deca
do
    echo "$i"
    ./launchers/test_synt "$i"
done
