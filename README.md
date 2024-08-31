# sins2-dataminer
Data miner/parser for Sins of a Solar Empire 2. Very much WIP. 

Intent is to be able to scan the C:\Program Files (x86)\Steam\steamapps\common\Sins2 directory and produce a set of json 
files that can be used as a datasource for a wiki.

Currently in use by https://strategywiki.org/wiki/Sins_of_a_Solar_Empire_2.

Note: If you want to use this, I'd highly recommend copying the following directories into `external-resources\steamdir` 
project so that your IDE can index them and you can actually find stuff in them:

- C:\Program Files (x86)\Steam\steamapps\common\Sins2\entities
- C:\Program Files (x86)\Steam\steamapps\common\Sins2\localized_text

The application won't read from these files, but it really helps with development just to be able to easily search them.

I'm not checking them into the repository to avoid copyright issues.

### Prerequisites
1. Have JDK 21 installed.
2. Have git installed.
3. Have maven installed.

### Building

1. Run `mvn clean install`

### Running from source

1. Set your steamapps path if it's different than mine with `-sd "C:\Program Files (x86)\Steam\steamapps\common\Sins2\"`(quotes are important if you have spaces in the path like me.)
2. Run `Main.java`
3. Json files will show up in `.\wiki\` unless you override the default with `-o other\path`

### Executing the release jar

You'll need JRE 21 installed for this, but that's it.

1. Download latest release jar from [github](https://github.com/dshaver1/sins2-dataminer/releases/).
2. Open a command line.
3. Change directories to wherever you downloaded the jar.
4. Execute the jar with `java --enable-preview -jar sins2-dataminer-0.1.jar`
5. Find the wiki jsons in the `wiki` directory under where you executed the jar.