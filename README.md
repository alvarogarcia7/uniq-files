# Uniq-Files

The domain is about files, hash (md5) and removing duplicated files

## Purpose

To generate a script to remove the duplicated files [1]

## Scope

### In scope

  * To read the input format (see input format) TODO
  * Generate a script to keep the last file with the same hash. Remove the rest of the files with the same hash

### Out of scope

  * To detect which files are equal. That is done through the input format (see input format) TODO
  * To delete the files in itself. That is the responsibility of the program's operator

## Assumptions

  * About the hash function:
    * Two files with the same contents produce the same hash value
    * Two files with different contents produce different hash values. [1]

## Run book

### Pre-processing

If you're using a md5 for mac, standardize the input format like so:

```
%s/^MD5 (\([^)]\+\)) = \(.*\)/\1 \2/
```

[1]: This program is not opinionated on which hash function should be used. It is suggested to use a hash function
with few (or none at all) hash collisions.


