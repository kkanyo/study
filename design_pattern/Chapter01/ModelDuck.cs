namespace CS_Study.Chapter01
{
    public class ModelDuck : Duck
    {
        public ModelDuck()
        {
            flyBehavior = new FlyNoWay();
            quackBehavior = new QuackDuck();
        }

        public override void Display()
        {
            Console.WriteLine( "It is model duck" );
        }

        public void Quack()
        {
            quackBehavior.Quack();
        }
    }
}
