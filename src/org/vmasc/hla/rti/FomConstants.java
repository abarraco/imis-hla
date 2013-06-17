package org.vmasc.hla.rti;

public class FomConstants
{
	public static final String FEDERATION_JNTC2 = "JNTC2";
	public static final String FEDERATION_TEST = "test";
	
	/*
	 * BASE ENTITY INSTANCE CONSTANTS
	 */
	/** The Constant BASEENTITY_CLASS_NAME. */
	public static final String BASEENTITY_CLASS_NAME = "BaseEntity";

	/** The Constant BASEENTITY_ATTRIBUTE_ACCELERATIONVECTOR. */
	public static final String BASEENTITY_ATTRIBUTE_ACCELERATIONVECTOR = "AccelerationVector";

	/** The Constant BASEENTITY_ATTRIBUTE_DEADRECKONINGALGORITHM. */
	public static final String BASEENTITY_ATTRIBUTE_DEADRECKONINGALGORITHM = "DeadReckoningAlgorithm";
	
	/** The Constant BASEENTITY_ATTRIBUTE_ORIENTATION. */
	public static final String BASEENTITY_ATTRIBUTE_ORIENTATION = "Orientation";
	
	/** The Constant BASEENTITY_ATTRIBUTE_WORLDLOCATION. */
	public static final String BASEENTITY_ATTRIBUTE_WORLDLOCATION = "WorldLocation";
	
	/** The Constant BASEENTITY_ATTRIBUTE_VELOCITYVECTOR. */
	public static final String BASEENTITY_ATTRIBUTE_VELOCITYVECTOR = "VelocityVector";
	
	/** The Constant BASEENTITY_ATTRIBUTE_CLIENTID. */
	public static final String BASEENTITY_ATTRIBUTE_CLIENTID = "ClientId";
	
	/** The Constant BASEENTITY_ATTRIBUTE_TIME. */
	public static final String BASEENTITY_ATTRIBUTE_TIME = "Time";

	/** The Constant BASEENTITY_ATTRIBUTE_ARRAY. */
	public static final String[] BASEENTITY_ATTRIBUTE_ARRAY = { BASEENTITY_ATTRIBUTE_ACCELERATIONVECTOR,
		BASEENTITY_ATTRIBUTE_DEADRECKONINGALGORITHM, BASEENTITY_ATTRIBUTE_ORIENTATION, BASEENTITY_ATTRIBUTE_WORLDLOCATION, BASEENTITY_ATTRIBUTE_VELOCITYVECTOR, BASEENTITY_ATTRIBUTE_CLIENTID, BASEENTITY_ATTRIBUTE_TIME};
	
	/*
	 * WEAPON FIRE INTERACTION CONSTANTS
	 */
	/** The Constant WEAPONFIRE_CLASS_NAME. */
	public static final String WEAPONFIRE_CLASS_NAME = "WeaponFire";

	/** The Constant WEAPONFIRE_PARAMETER_EVENTIDENTIFIER. */
	public static final String WEAPONFIRE_PARAMETER_EVENTIDENTIFIER = "EventIdentifier";

	/** The Constant WEAPONFIRE_PARAMETER_FIRECONTROLSOLUTIONRANGE. */
	public static final String WEAPONFIRE_PARAMETER_FIRECONTROLSOLUTIONRANGE = "FireControlSolutionRange";
	
	/** The Constant WEAPONFIRE_PARAMETER_FIREMISSIONINDEX. */
	public static final String WEAPONFIRE_PARAMETER_FIREMISSIONINDEX = "FireMissionIndex";
	
	/** The Constant WEAPONFIRE_PARAMETER_FIRINGLOCATION. */
	public static final String WEAPONFIRE_PARAMETER_FIRINGLOCATION = "FiringLocation";
	
	/** The Constant WEAPONFIRE_PARAMETER_FIRINGOBJECTIDENTIFIER. */
	public static final String WEAPONFIRE_PARAMETER_FIRINGOBJECTIDENTIFIER = "FiringObjectIdentifier";
	
	/** The Constant WEAPONFIRE_PARAMETER_FUSETYPE. */
	public static final String WEAPONFIRE_PARAMETER_FUSETYPE = "FuseType";
	
	/** The Constant WEAPONFIRE_PARAMETER_INITIALVELOCITYVECTOR. */
	public static final String WEAPONFIRE_PARAMETER_INITIALVELOCITYVECTOR = "InitialVelocityVector";
	
	/** The Constant WEAPONFIRE_PARAMETER_MUNITIONOBJECTIDENTIFIER. */
	public static final String WEAPONFIRE_PARAMETER_MUNITIONOBJECTIDENTIFIER = "MunitionObjectIdentifier";
	
	/** The Constant WEAPONFIRE_PARAMETER_MINUTIONTYPE. */
	public static final String WEAPONFIRE_PARAMETER_MINUTIONTYPE = "MunitionType";
	
	/** The Constant WEAPONFIRE_PARAMETER_QUANTITYFIRED. */
	public static final String WEAPONFIRE_PARAMETER_QUANTITYFIRED = "QuantityFired";
	
	/** The Constant WEAPONFIRE_PARAMETER_RATEOFFIRE. */
	public static final String WEAPONFIRE_PARAMETER_RATEOFFIRE = "RateOfFire";
	
	/** The Constant WEAPONFIRE_PARAMETER_TARGETOBJECTIDENTIFIER. */
	public static final String WEAPONFIRE_PARAMETER_TARGETOBJECTIDENTIFIER = "TargetObjectIdentifier";
	
	/** The Constant WEAPONFIRE_PARAMETER_WARHEADTYPE. */
	public static final String WEAPONFIRE_PARAMETER_WARHEADTYPE = "WarheadType";

	/** The Constant WEAPONFIRE_PARAMETER_ARRAY. */
	public static final String[] WEAPONFIRE_PARAMETER_ARRAY = {
		WEAPONFIRE_PARAMETER_EVENTIDENTIFIER, WEAPONFIRE_PARAMETER_FIRECONTROLSOLUTIONRANGE, WEAPONFIRE_PARAMETER_FIREMISSIONINDEX, WEAPONFIRE_PARAMETER_FIRINGLOCATION, WEAPONFIRE_PARAMETER_FIRINGOBJECTIDENTIFIER,
		WEAPONFIRE_PARAMETER_FUSETYPE, WEAPONFIRE_PARAMETER_INITIALVELOCITYVECTOR, WEAPONFIRE_PARAMETER_MUNITIONOBJECTIDENTIFIER, WEAPONFIRE_PARAMETER_MINUTIONTYPE, WEAPONFIRE_PARAMETER_QUANTITYFIRED, 
		WEAPONFIRE_PARAMETER_RATEOFFIRE, WEAPONFIRE_PARAMETER_TARGETOBJECTIDENTIFIER, WEAPONFIRE_PARAMETER_WARHEADTYPE};
}
