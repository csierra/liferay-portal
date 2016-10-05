import Control.Monad (forM_)
import Data.Char

def x y =
  "public static <" ++ types ++ "> " ++ y ++ "<T> lift(Function" ++
    arity ++"<" ++ types ++ "> fun" ++ params ++ ") { return null; }"
  where
    arity = show x
    letters = map (chr . (+64)) [1 .. x]
    types = (letters >>= \x -> [x, ',', ' ']) ++ ['T']
    params = letters >>= \x -> ", "++ y ++ "<" ++ [x] ++ "> " ++ [chr(ord(x) + 32)]

defs x y = [1 .. x] >>= \n -> [def n y]

main = forM_ (defs 8 "ChoiceQuerier") putStrLn

