day=day$1
dirpath=src/main/kotlin/${day}
filepath=${dirpath}/${day}.kt

mkdir $dirpath
touch $filepath

echo "package ${day}\n" >> $filepath
echo "fun main(args: Array<String>) {" >> $filepath
echo "    println(\"Hello World on ${day}!\")" >> $filepath
echo "}" >> $filepath
