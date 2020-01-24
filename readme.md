- Akka/Actor greatly simplifies the multi thread player simulation, therefore I am using it in this project,
 however I am very new to it, so I am sure I have done a lot things wrong, but it seems to work :)
- I didn't implement shutDown/restart, so when the game ends, it will just stuck there.
- PlayerA still have some advantage even using Akka/Actor, due to message are always sent to PlayerA first
- I am not very happy about the mutable states in both GameBoard and Player, I am sure there're solution in Akka, like using `context`,
 but I couldn't get that working in a short amount of time.
- I didn't write unit tests for everything due to time constraint(Also partly because GameBoard and Player are doing side effects which make testing difficult)
- I am also not very happy with all the side effects GameBoard does(been an Actor directly which means sending and receiving messages),
 which makes it very hard to test and debug, if given more time, I would like to extract all the state changing behaviour away from IO
- I put some comments around key places as we don't know each other's coding style, but I didn't explain everything,
 as I believe the code here isn't too complicated, and should all be readable and understandable, I also believe good code should be self documented.