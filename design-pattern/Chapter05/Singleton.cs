namespace CS_Study.Chapter05
{
    // sealed 키워드로 상속을 제한하여
    // 하나의 클래스가 하나의 인스턴스를 갖도록 보장
    sealed class Singleton
    {

        // 생성자를 private으로 선언했으므로
        // Singleton에서만 클래스의 인스턴스 생성 가능
        private Singleton() { }

        // Singleton 클래스의 하나뿐인 인스턴스를 저장하는 정적 변수
        // static 키워드
        // : 메모리에 반드시 하나만 생성되는 것을 보장
        // 인스턴스 존재 검사는 필요하지 않음
        // 게으른 초기화 (Lazy initialization)
        // : 클래스 내의 멤버들에 대한 접근이 이뤄질 때 인스턴스가 생성
        // readonly 키워드
        // : 경쟁 상태로 인해 인스턴스 변수가 null인 상태로
        // 알고 있는 스레드가 인스턴스를 생성하는 것을 방지
        private static readonly Singleton mInstance = new Singleton();
    }
}
