#!/bin/sh

# Script qui automatise les tests context
# sur les -context contenus dans deca/context/valid/references/
# et dans deca/context/invalid/references/

# Répertoire de test
TEST_DIR="$(dirname "$0")"/../deca/context

# Test des -context
echo "Test CONTEXT"
for j in "valid" "invalid"
do
    echo "$j"
    VAL_DIR=$TEST_DIR/"$j"
    # Références comparatives
    REF_DIR=$VAL_DIR/references/
    # Création du répertoire de travail
    RES_DIR=$VAL_DIR/res
    mkdir $RES_DIR
    for i in $REF_DIR/*-context
    do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    rm -f $RES_DIR/"$nom"-context
	    touch $RES_DIR/"$nom"-context
	    ./launchers/test_context $VAL_DIR/"$nom".deca > $RES_DIR/"$nom"-context
	    DIFF=$(diff $RES_DIR/$nom-context $i)
	    # Inspiré de l'Ensiwiki pour tester la nullité d'une variable
	    if [ "${DIFF}"1 = 1 ]; then
		echo "$nom : Success"
	    else
		echo "$nom : Failure"
		echo "$DIFF"
	    fi
	fi
    done
    # Destruction répertoire de travail
    if [ -d "$RES_DIR" ]; then
	rm -r $RES_DIR
    fi
done
