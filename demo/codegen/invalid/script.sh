#! /bin/sh

echo '{'
for i in $(seq 1 9999)
do
    echo "int x$i;"
done
echo '}'
