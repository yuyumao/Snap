- I am very new to Akka/Actor, so not sure how to do a lot of things correctly, but it seems to work :)
- I didn't implement shutDown/replay, so when the game ends, it will just stuck there.
- PlayerA still have some advantage even using Akka/Actor, due to message are always sent to PlayerA first
- I am not very happy about the mutable states in both GameBoard and Player, I am sure there're some solution in Akka, like using `context`, but I couldn't get that working.
- I didn't write unit tests for everything due to time constraint
- I am also not very happy with all the side effects GameBoard does(been an Actor), which makes it very hard to test and debug, if given more time
I would like to extract all the state changing behaviour away from IO(send and receive message)