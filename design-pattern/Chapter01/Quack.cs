namespace CS_Study.Chapter01
{
    public class QuackDuck : IQuackBehavior
    {
        public void Quack()
        {
            Console.WriteLine( "Quack" );
        }
    }

    public class MuteQuack : IQuackBehavior
    {
        public void Quack()
        {
            Console.WriteLine( "-----" );
        }
    }

    public class Squack : IQuackBehavior
    {
        public void Quack()
        {
            Console.WriteLine( "Squack" );
        }
    }
}
