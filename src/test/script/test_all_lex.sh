#!/bin/sh

# Script qui automatise les tests lex
# sur les -lex contenus dans deca/syntax/valid/references/lex
# et dans deca/syntax/invalid/references/lex

# Répertoire de test
TEST_DIR="$(dirname "$0")"/../deca/syntax

# Test des -lex
echo "Test LEX"
for j in "valid" "invalid"
do
    echo "$j"
    VAL_DIR=$TEST_DIR/"$j"
    # Références comparatives
    REF_DIR=$VAL_DIR/references/lex
    # Création du répertoire de travail
    RES_DIR=$VAL_DIR/res
    mkdir $RES_DIR
    for i in $REF_DIR/*-lex
    do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    rm -f $RES_DIR/"$nom"-lex
	    touch $RES_DIR/"$nom"-lex
	    test_lex $VAL_DIR/"$nom".deca > $RES_DIR/"$nom"-lex
	    DIFF=$(diff $RES_DIR/$nom-lex $i)
	    # Inspiré de l'Ensiwiki pour tester la nullité d'une variable
	    if [ "${DIFF}"1 = 1 ]; then
		echo "$nom : Success"
	    else
		echo "$DIFF"
		echo "$nom : Failure"
	    fi
	fi
    done
    # Destruction répertoire de travail
    if [ -d "$RES_DIR" ]; then
	rm -r $RES_DIR
    fi
done

