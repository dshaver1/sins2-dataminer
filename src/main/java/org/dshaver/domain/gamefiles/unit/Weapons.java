package org.dshaver.domain.gamefiles.unit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weapons {
    List<Weapon> weapons;
}
