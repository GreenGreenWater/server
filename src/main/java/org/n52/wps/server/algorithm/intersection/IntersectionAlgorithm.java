/***************************************************************
This implementation provides a framework to publish processes to the
web through the  OGC Web Processing Service interface. The framework 
is extensible in terms of processes and data handlers. It is compliant 
to the WPS version 0.4.0 (OGC 05-007r4). 

Copyright (C) 2006 by con terra GmbH

Authors: 
	Bastian Schaeffer, Institute for Geoinformatics, University of
	Muenster, Germany
	


Contact: Albert Remke, con terra GmbH, Martin-Luther-King-Weg 24,
48155 Muenster, Germany, 52n@conterra.de

This printersectionogram is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
version 2 as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program (see gnu-gpl v2.txt); if not, write to
the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA or visit the web page of the Free
Software Foundation, http://www.fsf.org.

Created on: 13.06.2006
 ***************************************************************/
package org.n52.wps.server.algorithm.intersection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.server.AbstractAlgorithm;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;



public class IntersectionAlgorithm extends AbstractAlgorithm {
	
	private static Logger LOGGER = Logger.getLogger(IntersectionAlgorithm.class);
	
	public IntersectionAlgorithm() {
		super();
	}

	private List<String> errors = new ArrayList<String>();
	public List<String> getErrors() {
		return errors;
	}
	
	
	
	public Map<String, IData> run(Map<String, List<IData>> inputData) {
		/*----------------------Polygons Input------------------------------------------*/
		if(inputData==null || !inputData.containsKey("Polygons1")){
			throw new RuntimeException("Error while allocating input parameters");
		}
		List<IData> dataList = inputData.get("Polygons1");
		if(dataList == null || dataList.size() != 1){
			throw new RuntimeException("Error while allocating input parameters");
		}
		IData firstInputData = dataList.get(0);
				
		FeatureCollection polygons = ((GTVectorDataBinding) firstInputData).getPayload();
		
		/*----------------------LineStrings Input------------------------------------------*/
		if(inputData==null || !inputData.containsKey("Polygons2")){
			throw new RuntimeException("Error while allocating input parameters");
		}
		List<IData> dataListLS = inputData.get("Polygons2");
		if(dataListLS == null || dataListLS.size() != 1){
			throw new RuntimeException("Error while allocating input parameters");
		}
		IData firstInputDataLS = dataListLS.get(0);
				
		FeatureCollection lineStrings = ((GTVectorDataBinding) firstInputDataLS).getPayload();
		
		
		System.out.println("****************************************************************");
		System.out.println("intersection started");
		System.out.println("polygons size = " + polygons.size());
		System.out.println("lineStrings size = " + lineStrings.size());
		
		FeatureCollection featureCollection = DefaultFeatureCollections.newCollection();
		
		Iterator polygonIterator = polygons.iterator();
		int j = 1;
		while(polygonIterator.hasNext()){
			SimpleFeature polygon = (SimpleFeature) polygonIterator.next();
			Iterator lineStringIterator = lineStrings.iterator();
			int i = 1;
			System.out.println("Polygon = " + j +"/"+ polygons.size());
			while(lineStringIterator.hasNext()){
				//System.out.println("Polygon = " + j + "LineString =" + i +"/"+lineStrings.size());
				
				SimpleFeature lineString = (SimpleFeature) lineStringIterator.next();
				Geometry lineStringGeometry = null;
				if(lineString.getDefaultGeometry()==null && lineString.getAttributeCount()>0 &&lineString.getAttribute(0) instanceof Geometry){
					lineStringGeometry = (Geometry)lineString.getAttribute(0);
				}else{
					lineStringGeometry = (Geometry) lineString.getDefaultGeometry();
				}
				try{
					Geometry polygonGeometry = (Geometry) polygon.getDefaultGeometry();
					Geometry intersection = polygonGeometry.intersection(lineStringGeometry);
					Feature resultFeature = createFeature(intersection);
					if(resultFeature!=null){
					//	Iterator featureCollectionIterator = featureCollection.iterator();
					//	while(featureCollectionIterator.hasNext()){
					//		Feature existsingFeature = (Feature) featureCollectionIterator.next();
						/*	if(!existsingFeature.getDefaultGeometry().covers(intersection)){
								featureCollection.add(resultFeature);
							}
							if(existsingFeature.getDefaultGeometry().coveredBy(intersection)){
								featureCollectionIterator = null;
								featureCollection.remove(existsingFeature);
								featureCollection.add(resultFeature);
								break;
							}*/
							
					//	}
						
						featureCollection.add(resultFeature);
						System.out.println("result feature added. resultCollection = " + featureCollection.size());
					}
				}catch(Exception e){
						e.printStackTrace();
					}
				
				i++;
			}
			j++;
			//if(featureCollection.size()>10){
			//	break;
			//}
		}
		
		
		HashMap<String,IData> resulthash = new HashMap<String,IData>();
		resulthash.put("result", new GTVectorDataBinding(featureCollection));
		return resulthash;
	}
	
	private Feature createFeature(Geometry geometry) {
		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
		b.add("LineString", LineString.class);
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(b
				.buildFeatureType());
		builder.add(geometry);

		return builder.buildFeature(null);
	}
	
	
	public Class getInputDataType(String id) {
		return GTVectorDataBinding.class;
	
	}

	public Class getOutputDataType(String id) {
		return GTVectorDataBinding.class;
	}
	
}