namespace CS_Study.Chapter05
{
    public class SingletonDCL
    {
        // Singleton 클래스의 하나뿐인 인스턴스를 저장하는 정적 변수
        private volatile static SingletonDCL mDCLInstance;

        // 기타 인스턴스 변수

        // 생성자를 private으로 선언했으므로
        // Singleton에서만 클래스의 인스턴스 생성 가능
        private SingletonDCL() { }

        // 클래스의 인스턴스를 생성하여 반환
        public static SingletonDCL GetInstance()
        {
            if ( mDCLInstance == null )
            {
                lock ( typeof( SingletonDCL ) )
                {
                    // 잠금(lock)을 먼저 획득한 스레드가 코드를 배타적으로 수행할 수 있게 함
                    // 이미 생성된 인스턴스가 있다면 첫번째 점검( check)에서 코드를 종료,
                    // 부하를 줄이게 됨
                    // 대기하는 동안에 변경될지도 모르는 값을 lock() 내부에서 다시 확인
                    if ( mDCLInstance == null )
                    {
                        // 게으른 인스턴스 생성(Lazyinstantiation)
                        // 인스턴스가 필요한 상황에만 인스턴스를 생성
                        mDCLInstance = new SingletonDCL();
                    }
                }
            }
            return mDCLInstance;
        }

        // Singleton도 보통 클래스이므로
        // 다른 변수나 메소드 존재 가능
        // 기타 메소드
    }
}
