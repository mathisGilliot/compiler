#!/bin/sh

for i in $(cat ../deca/syntax/valid/perso/tests_max.txt)
do
    echo "$i"
    ./launchers/test_synt ../deca/syntax/valid/"$i" > "${i%.deca}"-synt
    mv "${i%.deca}"-synt ../deca/syntax/valid/synt/references/
done

for i in $(cat ../deca/syntax/invalid/perso/tests_max.txt)
do
    echo "$i"
    ./launchers/test_synt ./../deca/syntax/invalid/"$i" 2> "${i%.deca}"-synt
    mv "${i%.deca}"-synt ../deca/syntax/invalid/synt/references/
done
