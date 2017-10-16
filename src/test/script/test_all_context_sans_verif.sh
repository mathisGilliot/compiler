#!/bin/sh

for i in ../deca/context/valid/*.deca
do
    echo "$i"
    ./launchers/test_context "$i"
done

for i in ../deca/context/invalid/*.deca
do
    echo "$i"
    ./launchers/test_context "$i"
done
