/**
 * Ein MusicTitle (Musiktitel) ist eine Baumstruktur, die
 * folgendermassen aufgebaut sein kann:
 * 
 *
 *
 * MusicTitle      --- GenreList ------------- Genre1		
 * (FormalTitle)                |_____________ Genre2		
 * (IndividualTitle)                            ...     	              
 * 
 *                 --- InstrumentationList --- Instrument1	
 *                                         |__ Instrument2	
 *                                             ...			
 *                 --- AdditionalInformation				
 *                     (DateOfComposition)					
 *                     (Key)								
 *                     (OpusNumber)							
 *                     (SerialNumber)						
 *                     (ThematicIndexNumber)				
 * 
 *                 --- PartOfWork ------------ MusicTitle1 ----------- ...	
 *                                |___________ MusicTitle2 ----------- ...	
 * 																			
 *                 --- Version --------------- GenreList ------------- ... 	
 *                             |______________ InstrumentationList --- ...	
 *                             |______________ AdditionalInformation - ...	
 *                 															
 *                 --- Arrangement											
 *                 															
 *                 --- Comment												
 * 
 * 
 */    
package de.dnb.music;

