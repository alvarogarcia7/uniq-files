# Uniq-Files

The domain is about files, hash (md5) and removing duplicated files

## Purpose

To generate a script to remove the duplicated files

## Scope

### In scope

  * To read the input format (see input format) TODO
  * Generate a script to keep the last file with the same hash. Remove the rest of the files with the same hash

### Out of scope

  * To detect which files are equal. That is done through the input format (see input format) TODO

## Run book

### Pre-processing

If you're using a md5 for mac, standarize the input format like so:

```
%s/^MD5 (\([^)]\+\)) = \(.*\)/\1 \2/
```


