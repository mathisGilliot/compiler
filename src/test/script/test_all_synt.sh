#!/bin/sh

# Script qui automatise les tests synt
# sur les -synt contenus dans deca/syntax/valid/references/synt
# et dans deca/syntax/invalid/references/synt

# Répertoire de test
TEST_DIR="$(dirname "$0")"/../deca/syntax

# Test des -synt
echo "Test SYNT"
compteur=0
for j in "valid" "invalid"
do
    echo "$j"
    VAL_DIR=$TEST_DIR/"$j"
    # Références comparatives
    REF_DIR=$VAL_DIR/references/synt
    # Création du répertoire de travail
    RES_DIR=$VAL_DIR/res
    mkdir $RES_DIR
    for i in $REF_DIR/*-synt
    do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    rm -f $RES_DIR/"$nom"-synt
	    touch $RES_DIR/"$nom"-synt
	    test_synt $VAL_DIR/"$nom".deca > $RES_DIR/"$nom"-synt
	    DIFF=$(diff $RES_DIR/$nom-synt $i)
	    # Inspiré de l'Ensiwiki pour tester la nullité d'une variable
	    if [ "${DIFF}"1 = 1 ]; then
		echo "$nom : Success"
	    else
		echo "$nom : Failure"
		echo "$DIFF"
		compteur=1
	    fi
	fi
    done
    # Destruction répertoire de travail
    if [ -d "$RES_DIR" ]; then
	rm -r $RES_DIR
    fi
    if [ $compteur -eq 0 ]; then
	exit 0
    else
	exit 1
    fi
done

