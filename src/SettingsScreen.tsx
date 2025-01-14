import React, {useCallback, useEffect, useState} from 'react';
import {View, Text, StyleSheet, TouchableOpacity, Modal,TextInput} from 'react-native';
import DeviceInfo from 'react-native-device-info';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';


const SettingsScreen: React.FC = () => {
  const appVersion = DeviceInfo.getVersion();
  const [modalVisible, setModalVisible] = useState(false);
  const [blockerView, setblocerView] = useState('1');

  const selectBlockerView = async (item) => {
    setblocerView(item);
    try {
      await AsyncStorage.setItem('blocerView', item);
      console.log('Data stored successfully');
    } catch (e) {
      console.error('Failed to save data', e);
    }
  };

  const getData = async () => {
    try {
      const value = await AsyncStorage.getItem('blocerView');
      console.log('value', value);
      if (!value) {
        setblocerView('1');
      }else{
        setblocerView(value);
      }

    } catch (e) {
      console.error('Failed to fetch data', e);
    }
  };

  useFocusEffect(useCallback(()=>{
    getData();
  },[]));

  return (
    <>
      <View style={styles.container}>
        <View style={styles.buttonContainer}>
          <View>
            <Text style={styles.title}>Select Blocker View</Text>
            <View style={styles.selectionLayout}>
              <TouchableOpacity style={[styles.selectItem,  {borderWidth: blockerView === '1' ? 1 : 0}]} onPress={() => selectBlockerView('1')}>
                <Text style={styles.title}>Calculattion View</Text>
              </TouchableOpacity>
             <TouchableOpacity style={[styles.selectItem,{borderWidth: blockerView === '2' ? 1 : 0}]} onPress={() => selectBlockerView('2')}>
                <Text style={styles.title}>Timer View</Text>
              </TouchableOpacity>
            </View>
          </View>
          <TouchableOpacity
            // onPress={() => setModalVisible(true)}
            style={styles.button}
            activeOpacity={0.5}>
            <Text style={styles.title}>Add more apps to block</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button} activeOpacity={0.5}>
            <Text style={styles.title}>Add more sites to block</Text>
          </TouchableOpacity>
        </View>
      </View>
      <View style={styles.appInfo}><Text style={{color:'gray'}}>{appVersion}</Text></View>

      <Modal animationType="slide" visible={modalVisible} transparent={true}>
        <View style={styles.modalContainer} />
        <View style={styles.modalViewContaienr}>
          <Text style={{color: 'black'}}>Enter the app name</Text>
          <TextInput style={styles.input} />
          <Text style={{color: 'black'}}>Enter the package name (bundle id)</Text>
          <TextInput style={styles.input} />
        </View>
      </Modal>
    </>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'flex-start',
    backgroundColor: 'white',
    padding: 24,
    width: '100%',
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'black',
  },
  subtitle: {
    fontSize: 14,
    textAlign: 'center',
  },

  buttonContainer: {
    flexDirection: 'column',
    gap: 16,
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    width: '100%',
  },
  button: {
    padding: 10,
    borderRadius: 10,
    backgroundColor: '#F5F5F5',
    width: '100%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '70%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },

  modalViewContaienr: {
    backgroundColor: 'white',
    width: '80%',
    height: '80%',
    position: 'absolute',
    top: '10%',
    left: '10%',
    alignItems: 'center',
    borderRadius: 16,
  },
  appInfo:{
    width:'100%',
    justifyContent:'center',
    alignItems:'center',
    position:'absolute',
    bottom:10,
  },
  selectItem:{
    borderColor:'black',
    borderWidth:1,
    padding:10,
    borderRadius:10,
    backgroundColor:'#F5F5F5',
  },
  selectionLayout:{flexDirection:'row',
    width:'100%',justifyContent:'space-between', alignItems:'center',marginTop:10, marginBottom:24},

});

export default SettingsScreen;
