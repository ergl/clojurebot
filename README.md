# ClojureBot

ClojureBot is a Telegram bot that lets you evaluate your own Clojure expressions, aynwhere, anytime.

## Usage

You'll need to get your own [Telegram API key](https://core.telegram.org/bots#3-how-do-i-create-a-bot).

ClojureBot uses [Environ](https://github.com/weavejester/environ), so you should set your `api-key` environment variable to your own key.

If you are running the bot using [leiningen](http://leiningen.org), you can add your key to your `profiles.clj` file, like so:

```clojure
{:dev
 {:env {:api-key "your-api-key"}}}
```

ClojureBot also uses [clojail](https://github.com/Raynes/clojail). From the README:

>Because clojail employs the JVM's built in sandboxing, you'll need to have a ~/.java.policy file to define permissions for your own code. If you don't do this, you'll get security exceptions. I've included a very liberal example.policy file that you can just copy over to ~/.java.policy.

Then, just `lein run` inside your project folder.

## Limitations

All the clojure code you pass to the bot is evaluated in a sandbox.

This specific implementation disallows the creation of `defs` and all filesystem and network access. Long-running processes will be terminated (issuing a Timeout Exception).

You can override this by changing the tester used in `sandbox.clj`.

Just change the `clj-telegrambot-tester` variable to include your preferred clojail tester.

## License

Copyright © 2015 Borja de Régil.

The use and distributon for this software are covered by the [Eclipse Public License 1.0](https://www.eclipse.org/legal/epl-v10.html).

See also [LICENSE](./LICENSE)

ClojureBot includes code from [clj-slackbot](https://github.com/verma/clj-slackbot), which is  
Copyright © 2014 Uday Verma. Licensed under the same terms as Clojure (EPL).
