package processor.memorysystem;

public class CacheLine {
    int[] datas;
    int[] tags;
    int[] TSLAs;
    int associativity;

    public CacheLine(int associativity) {
        this.associativity = associativity;
        datas = new int[associativity];
        tags = new int[associativity];
        TSLAs = new int[associativity];

        for(int i = 0; i < associativity; i++)
        {
            datas[i] = 0;
            tags[i] = -1;
            TSLAs[i] = 0;
        }
    }

    public int getWay(int tag) { 
        for(int i = 0; i < associativity; i++){
            if(this.tags[i] == tag){
                return i;
            }
        }

        return -1;
    }

    public int getData(int way) {
        TSLAs[way] = 0;
        return datas[way];
    }

    public void setData(int way, int data) {
        datas[way] = data;
        TSLAs[way] = 0;
    }

    public void setTag(int way, int tag) {
        tags[way] = tag;
    }

    public int getLRUWay() {
        int maxWay = 0;
        for(int i = 0; i < associativity; i++){
            if(TSLAs[i] > TSLAs[maxWay]){
                maxWay = i;
            }
        }
        return maxWay;
    }

    public void incrementTSLA() {
        for(int i = 0; i < associativity; i++){
            TSLAs[i]++;
        }
    }
}
