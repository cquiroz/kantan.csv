package tabulate.laws.discipline

import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws
import tabulate.CellEncoder
import tabulate.laws._

trait CellEncoderTests[A] extends Laws {
  def laws: CellEncoderLaws[A]
  implicit def arbA: Arbitrary[A]
  implicit def arbExpectedA: Arbitrary[ExpectedCell[A]]

  def cellEncoder[B: Arbitrary, C: Arbitrary]: RuleSet = new DefaultRuleSet(
    name = "cellEncoder",
    parent = None,
    "encode"             -> forAll(laws.encode _),
    "encode identity"    -> forAll(laws.encodeIdentity _),
    "encode composition" -> forAll(laws.encodeComposition[B, C] _)
  )
}

object CellEncoderTests {
  def apply[A](implicit a: Arbitrary[ExpectedCell[A]], c: CellEncoder[A]): CellEncoderTests[A] = new CellEncoderTests[A] {
    override def laws = CellEncoderLaws[A]
    override implicit def arbExpectedA = a
    override implicit def arbA = Arbitrary(a.arbitrary.map(_.value))
  }
}