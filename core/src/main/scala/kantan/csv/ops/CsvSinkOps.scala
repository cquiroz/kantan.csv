/*
 * Copyright 2016 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kantan.csv.ops

import kantan.csv._
import kantan.csv.engine.WriterEngine

/** Provides useful syntax for that types that have implicit instances of [[CsvSink]] in scope.
  *
  * The most common use case is to turn a value into a [[CsvWriter]] through [[asCsvWriter]]:
  * {{{
  *   val f: java.io.File = ???
  *   f.asCsvWriter[List[Int]](',', true)
  * }}}
  *
  * A slightly less common use case is encode an entire collection to CSV through [[writeCsv]]:
  * {{{
  *   val f: java.io.File = ???
  *   f.writeCsv[List[Int]](List(List(1, 2, 3), List(4, 5, 6)), ',', true)
  * }}}
  */
final class CsvSinkOps[A](val a: A) extends AnyVal {
  /** Shorthand for [[CsvSink.writer]]. */
  def asCsvWriter[B: RowEncoder](sep: Char, header: String*)
                                (implicit oa: CsvSink[A], e: WriterEngine): CsvWriter[B] =
    oa.writer(a, sep, header:_*)

  /** Shorthand for [[CsvSink.write]]. */
  def writeCsv[B: RowEncoder](rows: TraversableOnce[B], sep: Char, header: String*)
                             (implicit oa: CsvSink[A], e: WriterEngine): Unit =
    oa.write(a, rows, sep, header:_*)
}

trait ToCsvSinkOps {
  implicit def toCsvOutputOps[A](a: A): CsvSinkOps[A] = new CsvSinkOps(a)
}

object sink extends ToCsvSinkOps
