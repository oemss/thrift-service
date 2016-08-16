namespace java my.MyService

struct rt {
    1: string id
    2: string name
}

typedef list<rt> seq

service MyServ {

    void add(1: rt idR,2: rt idT)

    void delete(1: rt idR,2: rt idT)

    seq listT(1: rt idR)

    seq listR(1: seq lstT)

}



