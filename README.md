# sins2-dataminer
Data miner/parser for Sins of a Solar Empire 2. Very much WIP. 

Intent is to be able to scan the C:\Program Files (x86)\Steam\steamapps\common\Sins2 directory and produce a set of json 
files that can be used as a datasource for a wiki.

Currently in use by https://strategywiki.org/wiki/Sins_of_a_Solar_Empire_2.

Note: If you want to use this, I'd highly recommend copying the following directories into `resources\steamdir` 
project so that your IDE can index them and you can actually find stuff in them:

- C:\Program Files (x86)\Steam\steamapps\common\Sins2\entities
- C:\Program Files (x86)\Steam\steamapps\common\Sins2\localized_text

### Prerequisites
1. Have JDK 21 installed.
2. Have git installed.
3. Have maven installed.

### Building

1. Run `mvn clean install`

### Running

1. Set your steamapps path if it's different than mine with `-Dsteamdir='C:\Program Files (x86)\Steam\steamapps\common\Sins2\'`
2. Run `Main.java`
3. Json files will show up in `.\target\wiki\`
