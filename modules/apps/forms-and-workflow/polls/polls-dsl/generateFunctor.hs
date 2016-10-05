import Control.Monad (forM_)
import Data.Char

def x y = "public static <" ++ types ++ "> " ++ y ++ "<T> lift(Function" ++ (show x) ++"<" ++ types ++ "> fun" ++ params ++ ") { return null; }"
  where
    letters = map (chr . (+64)) [1 .. x]
    types = (letters >>= \x -> [x, ',', ' ']) ++ ['T']
    params = letters >>= \x -> ", "++ y ++ "<" ++ [x] ++ "> " ++ [chr(ord(x) + 32)]
  
main = forM_ ([1 .. 8] >>= \x -> [def x "ChoiceQuerier"]) putStrLn
