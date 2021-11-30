day=day$1
dirpath=src/main/kotlin/${day}
filepath=${dirpath}/${day}.kt

mkdir $dirpath
touch $filepath

echo "package ${day}\n" >> $filepath
echo "import java.io.File\n" >> $filepath
echo "fun main(args: Array<String>) {" >> $filepath
echo "    val lines = File(\"files/${day}.txt\").readLines()" >> $filepath
echo "    println(\"Hello World on ${day}!\")" >> $filepath
echo "}" >> $filepath

touch files/${day}.txt
