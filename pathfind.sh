#
# find java classes from a given directory descending to all sub directories recursively
#
# launch the java util with the following parameters
# -c : class name to search with no .java or .class extensions
# -p : package segments to search
# -d : search root directory where the search will start from
# -v : verbose output
# this shell does not work well with the shell comman line passing - strange misbehavior in Mac
# if this does not work well, create an alias like
# alias jpath='java -jar C:\\Documents and Settings\\bjeong\\.m2\\repository\\pathfind\\pathfind\\1.0\\pathfind-1.0.jar'
java -jar "C:\\Documents and Settings\\bjeong\\.m2\\repository\\pathfind\\pathfind\\1.0\\pathfind-1.0.jar" $*
