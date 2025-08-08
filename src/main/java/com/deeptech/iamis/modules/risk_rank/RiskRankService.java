package com.deeptech.iamis.modules.risk_rank;

import com.deeptech.iamis.core.BaseCrudService;

/**
 * Service interface for managing RiskRanks in the system.
 * It extends {@link BaseCrudService} to provide common CRUD operations for {@link RiskRank} entities
 * and can define additional functionality to be implemented specific to RiskRank management.
 */
public interface RiskRankService
  extends BaseCrudService<RiskRankDto, RiskRank> {}
