#include "RCSwitch.h"

#define RFID_LIM_INF  95  
#define RDIF_LIMP_SUP 105  

//Num max de bits usados por cada variavel 
#define DESLOCAMENTO_RFID 24 
#define DESLOCAMENTO_TEMP 16   
#define DESLOCAMENTO_GAS  8         


struct InfoRF{
  int id;           //int
  int temperatura;  //float
  int chamas;       //int
  int gas;          //boolean 
} infoRF;


RCSwitch receptor = RCSwitch();

void setup(){
  Serial.begin(9600);
  receptor.enableReceive(0);  
  
}

boolean RFIDValido(long info){
  boolean valido = false;
  infoRF.id = (info & 2130706432)>> DESLOCAMENTO_RFID;
  if((infoRF.id >= RFID_LIM_INF) && (infoRF.id <= RDIF_LIMP_SUP)){
    valido = true;
  }

  return valido;
}

void enviarUSB(){
  //Inicializa o buffer de temanho do InfoRF com valores zerados
  char buffer[sizeof(InfoRF)] = {0};
  //Copia o conteudo de infoRF, de tamanho InfoRF para a variavel buffer
  memcpy(&buffer, &infoRF, sizeof(InfoRF));
  Serial.write("I");
  Serial.write((uint8_t*)&buffer,sizeof(InfoRF));
  Serial.write("F");
}

int extrairGas(long info){
  int gas = (info & 65280) >> DESLOCAMENTO_GAS;
  //estava retornando valores maiores que 100
  return gas>=100?100:gas;
}

int extrairChamas(long info){
  int chamas = (info & 255);
  return chamas;
}

int extrairTemperatura(long info){
    int temperatura = (info & 16711680) >> DESLOCAMENTO_TEMP; 
  return temperatura;
}

void loop(){
  if(receptor.available()){
    long info = receptor.getReceivedValue();
     if(info != -1){
        if(RFIDValido(info)){
          infoRF.temperatura = extrairTemperatura(info);
          infoRF.chamas = extrairChamas(info);
          infoRF.gas = extrairGas(info);  
          enviarUSB();
        }  
    }   
    receptor.resetAvailable();
  }
  delay(1000);
}

