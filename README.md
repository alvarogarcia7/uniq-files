# Uniq-Files

The domain is about files, hash (md5) and removing duplicated files

## Purpose

To generate a script to remove the duplicated files

## Run book

### Pre-processing

If you're using a md5 for mac, standarize the input format like so:

```
%s/^MD5 (\([^)]\+\)) = \(.*\)/\1 \2/
```


