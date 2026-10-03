package lib

/**
  * This file contains a parser for IDS (Ideographic Description Sequences) strings.
  * It defines an enum `IdsNode` to represent the structure of IDS strings, and
  * provides two parsing methods: `parseIterative` and `parseFast`. Difference being that
  * parseFast may be more CPU efficient, but parseIterative is more readable and easier to understand.
  * Goal is to 'get' the radicals and components of a character.
  */

import scala.collection.mutable
import scala.util.boundary, boundary.break

enum IdsNode:
    case Component(value: Char)
    case Operation(operator: Char, children: List[IdsNode])

object IdsParser:

    private val idcOperators: Map[Char, Int] = Map(
    '⿰' -> 2, '⿱' -> 2, '⿴' -> 2, '⿵' -> 2, '⿶' -> 2,
    '⿷' -> 2, '⿸' -> 2, '⿹' -> 2, '⿺' -> 2, '⿻' -> 2,
    '⿲' -> 3, '⿳' -> 3
    )

    def parseIterative(idsStr: String): Option[IdsNode] =
        boundary[Option[IdsNode]]:
            val stack = mutable.Stack[IdsNode]()
            for char <- idsStr.reverse do
                idcOperators.get(char) match
                case Some(arity) =>
                    if stack.size < arity then break(None) // malformed input
                    val children = (1 to arity).map(_ => stack.pop()).toList
                    stack.push(IdsNode.Operation(char, children))
                case None =>
                    stack.push(IdsNode.Component(char))
            if stack.size == 1 then Some(stack.pop()) else None

    def parseFast(idsStr: String): Option[IdsNode] =
        boundary[Option[IdsNode]]:
            val stack = mutable.ArrayDeque[IdsNode]()
            var i = idsStr.length - 1
            while i >= 0 do
                val char = idsStr.charAt(i)
                idcOperators.get(char) match
                case Some(arity) =>
                    if stack.size < arity then break(None)
                    // Extract arity items from the top of the stack
                    val children = List.fill(arity)(stack.removeLast())
                    stack.append(IdsNode.Operation(char, children))
                case None =>
                    stack.append(IdsNode.Component(char))
                i -= 1
            if stack.length == 1 then Some(stack.removeLast()) else None

