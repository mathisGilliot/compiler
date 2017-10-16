#!/bin/sh

# Script qui automatise les tests context
# sur les -context contenus dans deca/context/valid/references/
# et dans deca/context/invalid/references/

compteur=0

# Répertoire de test
TEST_DIR="$(dirname "$0")"/../deca/context

# Test des -context
echo "Test CONTEXT"

echo "Valid"
VAL_DIR=$TEST_DIR/"valid"
# Références comparatives
REF_DIR=$VAL_DIR/references/
# Création du répertoire de travail
RES_DIR=$VAL_DIR/res
mkdir $RES_DIR 2>> /dev/null
for i in $REF_DIR/*-context
do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    rm -f $RES_DIR/"$nom"-context
	    touch $RES_DIR/"$nom"-context
	    test_context $VAL_DIR/"$nom".deca > $RES_DIR/"$nom"-context
	    DIFF=$(diff $RES_DIR/$nom-context $i)
	    # Inspiré de l'Ensiwiki pour tester la nullité d'une variable
	    if [ "${DIFF}"1 = 1 ]; then
		      echo "$nom : Success"
	    else
		      echo "$nom : Failure"
		      echo "$DIFF"
          $compteur = $compteur + 1
	    fi
    fi
done

echo "Invalid"
VAL_DIR=$TEST_DIR/"invalid"
# Références comparatives
REF_DIR=$VAL_DIR/references/
# Création du répertoire de travail
RES_DIR=$VAL_DIR/res
mkdir $RES_DIR 2>> /dev/null
for i in $REF_DIR/*-context
do
	if [ -f "$i" ]; then
	    nom="$(echo "$i" | rev | cut -d "/" -f1 | rev | cut -d "-" -f1)"
	    rm -f $RES_DIR/"$nom"-context
	    touch $RES_DIR/"$nom"-context
	    ./launchers/test_context $VAL_DIR/"$nom".deca 2> $RES_DIR/"$nom"-context
	    DIFF=$(diff $RES_DIR/$nom-context $i)
	    # Inspiré de l'Ensiwiki pour tester la nullité d'une variable
	    if [ "${DIFF}"1 = 1 ]; then
		      echo "$nom : Success"
	    else
		      echo "$nom : Failure"
		      echo "$DIFF"
          $compteur = $compteur + 1
	    fi
    fi
done


# Destruction répertoire de travail
if [ -d "$RES_DIR" ]; then
    rm -r $RES_DIR
fi

exit $compteur
