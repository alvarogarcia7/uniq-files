## Uniq-Files

The domain is about files, hash (md5) and removing duplicated files

### Pre-processing

If you're using a md5 for mac, standarize the input format like so:

```
%s/^MD5 (\([^)]\+\)) = \(.*\)/\1 \2/
```


