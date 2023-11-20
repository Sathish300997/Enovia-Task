import java.util.Map;
import java.util.HashMap;

import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.json.JSONObject;
import com.matrixone.apps.domain.util.PropertyUtil;

import matrix.db.Context;
import matrix.util.StringList;
import matrix.db.JPO;

public class WhereUsedBrit_mxJPO {
	
	/**
	 * gets where used Objects
	 *
	 * @param context - the eMatrix Context object
	 * @param args - Holds the HashMap containing the following arguments
	 *          paramMap - contains ObjectId which are required for further processing.
	 * @return MapList - containing the WhereUsed objects Id
	 * @throws Exception if the operation fails
     */
    public MapList getWhereUsedBrit(Context context, String[] args) throws Exception{
        MapList FirstLevelId = new MapList();
		MapList SecondLevelId = new MapList();
		MapList returnMapList=new MapList();
		StringList slObjSelects=new StringList();
		slObjSelects.add(DomainConstants.SELECT_ID);
	    JSONObject objData;
	    String CharacteristicObjectId = null;
	    String RelationshipXVCMethod = PropertyUtil.getSchemaProperty("relationship_XVCMethod");
		String RelationshipCharacteristic = PropertyUtil.getSchemaProperty("relationship_Characteristic");
		
  	 try{
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
			String APMObjectId=(String)programMap.get("objectId");
			DomainObject domContextBus= new DomainObject(APMObjectId);
			String busWhere="current!='Obsolete'";
	    	FirstLevelId = domContextBus.getRelatedObjects( context,
                                               RelationshipXVCMethod,       //relationship
                                               "*",       // type
                                               slObjSelects,       // string list
                                               null,      
                                               true,     // to rel
                                               false,      // from rel
                                              (short)1,  // level
                                               "",
                                               null);
      if(FirstLevelId.size()>0)
	  {
        // second obj data
         for (int count=0;count<FirstLevelId.size(); count++)
            {
   	             Map objInfo = (Map)FirstLevelId.get(count);
                 objData = new JSONObject(objInfo);
                 CharacteristicObjectId=objData.get("id").toString();			 
		         DomainObject domContextBus1= new DomainObject(CharacteristicObjectId);
	             SecondLevelId = domContextBus1.getRelatedObjects( context,
                                                                   RelationshipCharacteristic,       //relationship
                                                                   "*",       // type
                                                                   slObjSelects,       // string list
                                                                   null,      
                                                                   true,     // to rel
                                                                   false,      // from rel
                                                                   (short)1,  // level
                                                                   busWhere, //object where
                                                                   null); 
                 returnMapList.addAll(SecondLevelId);
         
            }
	  }
     }catch(Exception exception){
            
            throw new FrameworkException(exception);
        }
        return returnMapList;
    }
    }