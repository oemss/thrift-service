namespace java my.MyService

struct rt {
    1: string id
    2: string name
}

typedef list<rt> seq

service MyServ {

    void add(1: string idR,2: string idT)

    void delete(1: string idR,2: string idT)

    seq listT(1: string idR)

    seq listR(1: list<string> lstT)

    void put(1: i16 where, 2: seq wt) // Функция для тетсирования


}



