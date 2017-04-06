# Uniq-Files

## Purpose

To generate a script to remove the duplicated files, based on file contents.

## Scope

### In scope

  * To read the input format (see [input format][#input-format])
  * Generate a script to keep the last file with the same hash. Remove the rest of the files with the same hash

### Out of scope

  * To detect which files are equal. That is done through the input format (see [input format][#input-format])
  * To delete the files in itself. That is the responsibility of the program's operator

## Assumptions

  * About the hash function:
    * Two files with the same contents produce the same hash value
    * Two files with different contents produce different hash values. [1]

## Run book

### Running the program

```bash
java -jar target/uberjar/uniq-files-0.1.0-SNAPSHOT-standalone.jar resources/example-1/md5.txt
```

### Input format

This program expects:

```
filename-1 hash-1
filename-2 hash-2
```

  * The filename and the hash are separated by one space
  * Multiple files can (SHOULD) be input at once

See [In Scope][In Scope] for the criteria of which file is kept


### Output format

The output format is a bash script, that will invoke `rm`. It can have comments. As of 2017-04, the program does not
guarantee the order of the output files matches the order of the input file. (Open an issue if you wish to expand the
 program. Or even better, send a pull request)

An example output:

```bash
➜  uniq-files git:(master) java -jar target/uberjar/uniq-files-0.1.0-SNAPSHOT-standalone.jar resources/example-1/md5.txt
# keep 2017-1.txt
rm 2016-1.txt
rm 2016-2.txt
# keep 2016-3.txt
# keep 2017-2.txt
```

### Pre-processing

If you're using a md5 for mac, standardize the input format like so:

```
%s/^MD5 (\([^)]\+\)) = \(.*\)/\1 \2/
```

[1]: This program is not opinionated on which hash function should be used. It is suggested to use a hash function
with few (or none at all) hash collisions.


## Implementation details

  * This program is to be used as another small command-line utility: this is a library more than a framework, given
  you don't lose control
