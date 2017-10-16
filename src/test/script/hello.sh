#! /bin/sh

# Auteur : gl27
# Version initiale : 09/01/2017

# Test hello_world.deca

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"
FILENAME=./src/test/deca/syntax/valid/hello_world

rm -f $FILENAME.ass 2>/dev/null
decac $FILENAME.deca || exit 1
if [ ! -f $FILENAME.ass ]; then
    echo "Fichier $FILENAME.ass non généré."
    exit 1
fi

resultat=$(ima $FILENAME.ass) || exit 1
#rm -f $FILENAME.ass

# On code en dur la valeur attendue.
attendu="Hello World"

if [ "$resultat" = "$attendu" ]; then
    echo "Tout va bien"
else
    echo "Résultat inattendu de ima:"
    echo "$resultat"
    exit 1
fi
