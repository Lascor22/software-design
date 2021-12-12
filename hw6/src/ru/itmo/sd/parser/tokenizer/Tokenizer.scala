package ru.itmo.sd.parser.tokenizer

import java.util

import ru.itmo.sd.parser.tokenizer.TokenizerState._

class Tokenizer {

  private var buffer: StringBuilder = new StringBuilder
  private var state: TokenizerState = _

  private val tokens: util.List[Token] = new util.ArrayList

  def parse(input: String): Unit = {
    input.toCharArray.zipWithIndex.forall(applyNext)
    applyNext(('$', input.length + 1))
  }

  def getTokens: util.List[Token] = tokens

  private def applyNext(input: (Char, Int)): Boolean = {
    val (char, index) = input

    val newState = char match {
      case id if IDENTIFIER.test(state, id) => IDENTIFIER;
      case num if NUMBER.test(state, num) => NUMBER;
      case br if BRACE.test(state, br) => BRACE;
      case op if OPERATION.test(state, op) => OPERATION;
      case ' ' => return true;
      case '$' =>
        this.flushBuffer()
        return true
      case _ => null;
    }
    if (newState == null) {
      val message = "Unexpected character `" + char +
        "` at index " + index
      throw new IllegalStateException(message)
    } else if (!newState.equals(state)) {
      this.flushBuffer()
      this.buffer = new StringBuilder
      this.state = newState
    }
    buffer.append(char)
    true
  }

  private def flushBuffer(): Unit = {
    if (state != null && buffer.nonEmpty) {
      tokens.add(state.apply(buffer.toString))
    }
  }
}
