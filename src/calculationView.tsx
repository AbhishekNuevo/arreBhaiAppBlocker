import { StyleSheet, Text, TextInput, View } from 'react-native';
import React, { useEffect,useState } from 'react';

const Operator = ['+', '-', '*', '/','*','*','+','+','*','-'];

const CalculationView = ({setUnlockTime}) => {
   const [result, setResult] = useState(0);
   const [value, setValue] = useState('');
   const [calculation, setCalculation] = useState('');


    const generateCalculation = () => {
      const num1 = Math.floor(Math.random() * 100);
      const num2 = Math.floor(Math.random() * 100);

      const operator = Operator[Math.floor(Math.random() * Operator.length)];
      let result;
      switch (operator) {
        case '+':
          result = num1 + num2;
          break;
        case '-':

          result = num1 > num2 ? num1 - num2 : num2 - num1;
          break;
        case '*':
          result = num1 * num2;
          break;
        case '/':
          result = Math.floor(num1 / num2);
          break;
        default:
          result = 0;
      }

      setResult(result);
      const printCaclulation = num1 > num2 ? `${num1} ${operator} ${num2}` : `${num2} ${operator} ${num1}`;
      setCalculation(printCaclulation);

    };

    useEffect(()=>{
        generateCalculation();
    },[]);

    useEffect(()=>{
        console.log('correct1',value, result.toString());
        if( value?.toString()?.length > 0 && result?.toString()?.length > 0 && (+value === +result)){
           console.log('correct2');
           setUnlockTime && setUnlockTime(60); //2 min
        //    RNExitApp.exitApp();
        }
    },[value,result]);

  return (
    <View style={{ alignItems:'center', width:'100%',  height:'40%' }}>
      <Text style={{fontSize:30, color:'black'}}>{calculation}</Text>
      <TextInput style={{fontSize:20, color:'black', backgroundColor:'#d1e8e5', width:'60%',borderRadius:10 }} value={value} keyboardType="numeric" onChangeText={text => setValue(text)} />
    </View>
  );
};

export default CalculationView;

const styles = StyleSheet.create({});
