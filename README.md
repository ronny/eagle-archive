# eagle-archive

A web service that can receive events from a [Rainforest Automation Eagle](https://rainforestautomation.com/rfa-z109-eagle/)
device (which itself receives information from electricity smart meters) and stores them in a database.

## Requirements

- Leiningen 2.6 with Clojure 1.8 (Java 8 Runtime)
- Cassandra 3.7

The versions above are the versions that I use, they're not
necessarily minimum versions.

If you're on the Mac and uses Homebrew, you can do the following to install them:

```
brew install leiningen cassandra
```

## Installation

1. Install all requirements and ensure they are working.
2. `git clone` this repository
3. `cd` to the newly created directory
4. See usage below

## Usage

There are no service or init scripts yet (PRs welcome 😀). To start the server manually:

```
lein ring server-headless 7000
```

Replace 7000 with any port of your choosing.

Then go to your Eagle device web-based management app, Settings, Cloud, add a new provider
(+), in the URL field enter `http://your-host-or-ip-address:7000` replacing the host and port
with your specific setup.

You should start seeing output like this on the server stdout:

```
{:demand-in-kw 1.381M, :meter-mac-id "00:12:34:56:78:90:AB:CD"}
200  :InstantaneousDemand
200  :PriceCluster
200  :MessageCluster
200  :DeviceInfo
```

## Development

### Running tests

```
lein midje :autotest :config test/midje.config.clj
```

## References

The HTTP endpoint that receives data from the device implements APIs described in
http://rainforestautomation.com/wp-content/uploads/2014/07/EAGLE-Uploader-API_06.pdf

## License

Copyright © Ronny Haryanto

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
