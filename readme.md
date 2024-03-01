# Directoryparser
is the lightweight console app to recursively scan your directory tree with two purposes:
1. count the document files
2. count the pages inside those document files

At the moment you can parse .docx and .pdf files, all other files ignored.

### Used libraries:
Application is written in java and build using the maven.
- apache commons-cli - for parsing args
- apache commons-io - to separate file extensions
- apache poi - to parse .docx files
- apache pdfbox - to parse .pdf files

### Usage
```
usage: derectoryparser -p path -e extension [extension...]
-e,--extensions <extension>   extensions to parse (space separated, without dot)
-h,--help                     show this message
-p,--path <path>              root parsing directory tree
```

### Contributing
Feel free to add modules to parse additional file types. To do so you need:
1. add file type to enum com/greenjack/FileTypes.java
2. implement the com/greenjack/pagecounters/PageCounter.java interface using appropriate library to access desired files
3. include the library dependency in pom.xml
