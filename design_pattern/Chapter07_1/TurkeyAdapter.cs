namespace CS_Study.Chapter07_1
{
    // 적응시킬 형식의 인터페이스를 구현
    // 즉, 클라이언트에서 원하는 인터페이스를 구현
    internal class TurkeyAdapter : Duck
    {
        
        Turkey turkey;

        // 기존 형식 객체의 레퍼런스 필요
        public TurkeyAdapter(Turkey turkey )
        {
            this.turkey = turkey;
        }

        public void Quack()
        {
            turkey.Gobble();
        }

        public void Fly()
        {
            // 적응시킬 형식에 대응하도록 기능을 수정
            for (int i = 0; i < 5; i++)
            {
                turkey.Fly();
            }
        }
    }
}
