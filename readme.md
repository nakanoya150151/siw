# How to Use

## Setup 
### Bitcoind for Windows
Download client and install to local PC.  
https://bitcoin.org/bin/bitcoin-core-0.15.0.1/bitcoin-0.15.0.1-win64-setup.exe

Create bitcoin.conf and place it.
```sh
regtest=1
server=1
rpcuser=siw
rpcpassword=siw
zmqpubhashblock=tcp://127.0.0.1:28332
```

Boot bitcoind with regtest-mode
```sh
$ cd ${bitcoin intall dir}/daemon
$ ./bitcoind.exe -conf=/path/to/bitcoin.conf
```

### node-manager
Clone repository (siw means simpex wallet...) 
```sh
$ git clone https://github.com/nakanoya150151/siw.git
```

#### for intellij
Set up lombok  
see. http://siosio.hatenablog.com/entry/2013/12/23/000054

#### for VSCode
Edit VSCode Setting (Add the following line)  
※Change the local path according to your own environment
```json
{
    "java.home": "C:/Program Files/Java/jdk1.8.0_144",
    "java.jdt.ls.vmargs": "-javaagent:C:/Users/nakanoya150151/work/repos/siw/lib/lombok.jar -Xbootclasspath/a:C:/Users/nakanoya150151/work/repos/siw/lib/lombok.jar"
}
```

Boot process
```sh
$ cd ${repository clone dir}
$ ./gradlew bootRun
```

## Mock Bitcoin Network with bitcoin-cli

### Generate Block
```sh
$ cd ${bitcoin intall dir}/daemon
$ ./bitcoin-cli.exe -conf=/path/to/bitcoin.conf generate 1
```

### Create Transaction
```sh
$ cd ${bitcoin intall dir}/daemon
$ ./bitcoin-cli.exe -conf=/path/to/bitcoin.conf getnewaddress
mh5tKYs3ywj3y4bWrKsV32ocNx4u2jEYYY … -> new Address.

$ bitcoin-cli -conf=/path/to/bitcoin.conf sendtoaddress mh5tKYs3ywj3y4bWrKsV32ocNx4u2jEYYY 0.01 … send amount
676ac762ea5368a0a7fe422ecdb67bf7a734b1a49d2fdf55d723132063b44d7b … transaction ID
```