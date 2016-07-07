#!/bin/sh

echo "Starting Conversion"

for src in `find markdown -name '*.png'`; do
   dst=`echo $src|sed -e 's/markdown/html/g'`
   echo "Copy $src => $dst"
   mkdir -p `dirname $dst`
   cp -f $src $dst
done


for mdfile in `find . -name '*.md'`; do
   htmlfile=`echo $mdfile|sed -e 's/md/html/g'|sed -e 's/markdown/html/g'`
   echo "Convert $mdfile => $htmlfile"
   mkdir -p `dirname $htmlfile`
   pandoc -o $htmlfile $mdfile
done

echo "Conversion done"
