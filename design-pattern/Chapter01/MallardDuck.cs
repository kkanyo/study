namespace CS_Study.Chapter01
{
    public class MallardDuck : Duck
    {
        public MallardDuck()
        {
            flyBehavior = new FlyWithWings();
            quackBehavior = new QuackDuck();
        }

        public override void Display()
        {
            Console.WriteLine( "It is mallard duck" );
        }
    }
}
