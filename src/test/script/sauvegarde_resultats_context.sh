
#!/bin/sh

for i in $(cat ../deca/context/valid/perso/tests_max.txt)
do
    echo "$i"
    ./launchers/test_context ../deca/context/valid/"$i" > "${i%.deca}"-context
    mv "${i%.deca}"-context ../deca/context/valid/references/
done

for i in $(cat ../deca/context/invalid/perso/tests_max.txt)
do
    echo "$i"
    ./launchers/test_context ./../deca/context/invalid/"$i" 2> "${i%.deca}"-context
    mv "${i%.deca}"-context ../deca/context/invalid/references/
done
